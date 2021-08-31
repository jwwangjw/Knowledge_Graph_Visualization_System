<template>
	<div style="background: #f0f2f5">

		<a-card class="usr-card">
			<div class="usr-card-body">
				<div class="usr-card-avatarHolder">
					<div class="avatar"><img src="@/assets/avatar.jpg"/></div>
					<div class="usrname">{{this.username}}</div>
				</div>
			</div>
			<div class="usr-card-detail">
				<p><i class="title"></i>程序猿</p>
				<p><i class="group"></i>南京大学 - 软件学院</p>
				<p><i class="address"></i>江苏省南京市</p>
			</div>
			<a-divider/>
			<div>
				<p style="font-weight: 700">项目成员</p>
				<ul>
					<li>王海天(PM)</li>
					<li>原铭骏</li>
					<li>王嘉伟</li>
					<li>浦隽轩</li>
				</ul>
			</div>
			<a-divider/>
			<div>
				<p style="font-weight: 700">项目描述</p>
				<p style="padding-left: 16px;padding-right: 16px">本网站为COIN知识图谱可视化系统前端系统，采用Ant-Design设计风格。包括：</p>
				<ul>
					<li>欢迎页面</li>
					<li>知识图谱 - 力导图模式页面</li>
					<li>知识图谱 - 排版模式页面</li>
				</ul>
				<!--p style="padding-left: 16px;padding-right: 16px">目前暂不支持添加空白画布，需要根据格式导入图谱。点击"即刻开始"开启图谱可视化！</p-->
			</div>
		</a-card>
		<div class="hello-title">



			<a-card class="hello-start">
        <div class="block">
          <el-carousel height="150px">
            <el-carousel-item>
              <p style="color:white; font-weight: 700;font-size: 20px;text-align: center;line-height: 150px">

                欢迎进入CODING67系统！
              </p>

            </el-carousel-item>
            <el-carousel-item>
              <p style="color:white; font-weight: 700;font-size: 20px;text-align: center;line-height: 150px">
                点击即可上传生成知识图谱
              </p>
            </el-carousel-item>
          </el-carousel>
        </div>
        <p></p>
        <p></p>
				<p style="font-weight: 700;font-size: 20px">{{this.call}}</p>
				<a-divider/>
		<div>
			<a-row>
				<a-col :span="14">
					<input type="file" ref="readFile" style="display: none" v-on:change="getFile($event)">
					<a-card @click="importProj" style="width: 500px;height:300px;float: left">
						<div align="center">
							<img src = "@/assets/icons/upload.png" style=" height: 150px;width: 150px" />
						</div>
						<div align="center">
							<p>上传本地文件获取知识图谱</p>
							<p>1.选中结构化CSV文件可立即实现知识图谱上传</p>
							<p>2.若选择TXT文件,则进行知识提取后,上传知识图谱</p>
						</div>
					</a-card>
				</a-col>
				<a-col :span="10">
					<div class="hello-pic"><img src="@/assets/graph.png"/></div>
				</a-col>
			</a-row>
		</div>
		<a-divider/>
		<p>点击上传框图导入项目，开始知识图谱可视化！注：暂不支持创建空白模板</p>

			</a-card>
			<a-card title="项目总览"  class="hello-proj">
				暂未实现
			</a-card>
		</div>

        <ChooseTripleModal ref="dataTrans"></ChooseTripleModal>
        <ChangeTriple></ChangeTriple>
        <vue-alert-loading :visible="loading" title="三元组正在构建..."/>
        <vue-alert-loading :visible="triplesLoading" title="知识图谱正在构建..."/>
	</div>

</template>

<script>
	import {addNewFileProjectAPI,addNewTxtProjectAPI} from "@/api/Info";
    import ChooseTripleModal from "../../components/WorkTree/chooseTripleModal";
    import ChangeTriple from "../../components/WorkTree/changeTriple";
	import {message} from "ant-design-vue";
	import callUtils from "../../utils/callUtils";
    import Cookies from "js-cookie";
	export default{
        components: {ChooseTripleModal,ChangeTriple},
		data(){
			return{
				data:'',
				call:'',
				file:'',
        visible: false,
        username: Cookies.get('storeUsername'),
        userId: Cookies.get('userId'),
        loading:false,
        triplesLoading:false,
			}
		},
		mounted() {
			const hour = new Date().getHours();
			if(hour>=0&&hour<5){
				this.call = "凌晨啦！睡一觉？"
			}else if(hour>=5&&hour<9){
				this.call = "一年之计在于晨。"
			}else if(hour>=9&&hour<11){
				this.call = "上午好！努力加油吧^_^"
			}else if(hour>=11&&hour<14){
				this.call = "中午好！"
			}else if(hour>=14&&hour<18){
				this.call = "下午好！别打盹哦"
			}else if(hour>=18&&hour<20){
				this.call = "傍晚，有没有一顿晚饭犒劳自己呢？"
			}else if(hour>=20&&hour<24){
				this.call = "晚上好！"
			}else{
				this.call = "好像获取时间出了点问题"
			}
		},
		methods:{
            showModal() {
                this.visible = true;
            },
            handleOk(e) {
                this.visible = false;
            },
			importProj(){
				console.log("add file")
				this.$refs.readFile.dispatchEvent(new MouseEvent("click"))
			},


			getFile(event){
                this.loading=true
				this.file = event.target.files[0];
                let begin=this.file.name.indexOf(".")
                let fileType = this.file.name.substring(begin,this.file.name.length)
				event.preventDefault();
				let formData=new FormData;
				formData.append("file",this.file);

				if(fileType === ".csv") {
                    addNewFileProjectAPI(formData,this.userId).then(res => {
                    if (res.data.success) {
                        this.loading=false
                        message.success('上传成功');
                        callUtils.$emit('getKGProject', 'Home')
                    }else {
                        message.error("上传失败");
                    }
                    });
                }
				else if(fileType === ".txt") {
                    this.loading=true
                    addNewTxtProjectAPI(formData,this.userId).then(res => {
                        if (res.data.success) {
                            message.success('上传成功');
                            this.loading=false
                            this.$store.commit('set_chooseTripleVisible',true);
                            this.$refs.dataTrans.init(res.data.content)
                            /*callUtils.$emit('getKGProject', 'Home')*/
                        }else {
                            message.error("上传失败");
                        }
                    });
                }
				this.file = '';
			},

		}
	}

</script>

<style lang="scss" scoped>
	.moduleAll{
		.onePart{
			width:25%;
			height: 100%;
			text-align:center;
			.cardMeta{
				display: flex;
				align-items: center;
				justify-content: center;
			}
		}
	}
	.usr-card{
		float: left;
		width: 25%
	}
	.usr-card-body{
		padding: 24px;
	}
	.usr-card-avatarHolder{
		text-align: center;
		margin-bottom: 24px;
	}
	.avatar{
		margin: 0px auto;
		width: 104px;
		height: 104px;
		margin-bottom: 20px;
		border-radius: 50%;
		overflow: hidden;
	}
	.usrname{
		color: rgba(0,0,0,.85);
		font-size: 20px;
		line-height: 28px;
		font-weight: 500;
		margin-bottom: 4px;
	}
	.usr-card-detail{
		padding-left: 24px;
		.title{
			background-position: 0 0;
		}
		.group{
			background-position: 0 -22px;
		}
		.address{
			background-position: 0 -44px;
		}
		i{
			position: absolute;
			height: 14px;
			width: 14px;
			left: 0;
			top: 4px;
			background: url(https://gw.alipayobjects.com/zos/rmsportal/pBjWzVAHnOOtAUvZmZfy.svg);
		}
		p{
			position: relative;
			margin-bottom: 8px;
			padding-left: 26px;
		}
	}
	.hello-title{
		margin-left: 24px;
		width: 72%;
		float: left;
	}
	.hello-font{
		margin-left: 25px;
		font-weight: 700;
		font-size: 50px;
		color: black;
		float: left;
		cursor: pointer;
	}
	.hello-pic{

		height: 240px
	}
	.hello-proj{
		margin-top: 18px;
		height: 250px;
	}
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 150px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #364d79;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #364d79;
  }

</style>
