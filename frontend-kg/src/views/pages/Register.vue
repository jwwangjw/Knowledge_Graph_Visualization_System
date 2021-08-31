<template>
  <div class="login_container">
    <div class="login_box">
      <!-- 登录表单区域 -->
      <el-form ref="loginForm" :rules="loginRules" :model="loginForm" size="mini">
        <!-- 用户名 -->
        <el-form-item prop="phone">
          <el-input v-model="loginForm.phone" placeholder="手机号" prefix-icon="iconfont icon-danju"></el-input>
        </el-form-item>
        <!-- 用户名 -->
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="用户名" prefix-icon="iconfont icon-user"></el-input>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" placeholder="密码" show-password  prefix-icon="iconfont icon-3702mima"></el-input>
        </el-form-item>
        <!-- 验证码 -->
        <el-form-item prop="code" :inline="true">
          <el-input v-model="loginForm.code" class="code" placeholder="验证码"></el-input>
          <div style="float: right ;margin-top: -5px;" @click="refreshCode" >
            <s-Identify :identifyCode="identifyCode"></s-Identify>
          </div>
        </el-form-item>
        <!-- 记住我 -->
        <el-form-item>
          <el-checkbox v-model="loginForm.rememberMe" label="记住我" class="rememberMe"></el-checkbox>
        </el-form-item>
        <!-- 登录按钮 -->
        <el-form-item>
          <el-button   @click.native.prevent="handleRegister" type="primary">注册</el-button>
        </el-form-item>
        <router-link to="/login">
          <p style="color: deepskyblue;textAlign: center;font-size: 12px;font-family: '微软雅黑 Light'">返回登录</p>
        </router-link>
      </el-form>
    </div>
    <vue-particles
        color="#fff"
        :particleOpacity="0.7"
        :particlesNumber="60"
        shapeType="circle"
        :particleSize="4"
        linesColor="#fff"
        :linesWidth="1"
        :lineLinked="true"
        :lineOpacity="0.4"
        :linesDistance="150"
        :moveSpeed="2"
        :hoverEffect="true"
        hoverMode="grab"
        :clickEffect="true"
        clickMode="push"
        class="lizi"
        style="height:100%"
    >
    </vue-particles>
  </div>
</template>

<script>

import SIdentify  from '../../components/identify'
import {addNewFileProjectAPI} from "@/api/Info";
import {registerAPI} from "@/api/User";
import {message} from "ant-design-vue";
import Cookies from 'js-cookie';
import { encrypt, decrypt } from '@/utils/jsencrypt';
import callUtils from "@/utils/callUtils";

export default {
  name: 'Register',
  data () {

    const validateCode = (rule, value, callback) => {
      if (this.identifyCode !== value) {
        this.loginForm.code = ''
        this.refreshCode()
        callback(new Error('请输入正确的验证码'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        phone:'',
        username: '',
        password: '',
        rememberMe: false,
        code: '',
        uuid: '',

      },
      loginRules: {
        phone: [
          { required: true, trigger: 'blur', message: '手机号不能为空' }
        ],
        username: [
          { required: true, trigger: 'blur', message: '用户名不能为空' }
        ],
        password: [
          { required: true, trigger: 'blur', message: '密码不能为空' }
        ],
        code: [{ required: true, trigger: 'change', message: '验证码不能为空' }]
      },

      identifyCodes: '1234567890',
      identifyCode: '',//找回密码图形验证码

    }
  },


  components: {
    's-Identify': SIdentify,
  },
  watch:{
    identifyCode(v)
    {
      this.isDebugLogin && (this.loginForm.code = v)
    }
  },
  methods: {
    randomNum(min, max) {
      return Math.floor(Math.random() * (max - min) + min)
    },
    refreshCode() {
      this.identifyCode = ''
      this.makeCode(this.identifyCodes, 4)
    },
    makeCode(o, l) {

      for (let i = 0; i < l; i++) {

        this.identifyCode += this.identifyCodes[
            this.randomNum(0, this.identifyCodes.length)
            ]
      }
    },
    handleRegister () {

      if(this.loginForm.code !== this.identifyCode){
        message.error("请输入正确的验证码");
      }
      else{
        registerAPI(this.loginForm).then(res => {
          //console.log(res)
          if (res.data.success) {
            if(this.loginForm.rememberMe){
              Cookies.set('username', this.loginForm.username, { expires: 30 })
              Cookies.set('password', encrypt(this.loginForm.password), { expires: 30 })
              Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 })
            }else {
              Cookies.remove('username')
              Cookies.remove('password')
              Cookies.remove('rememberMe')
            }

            message.success('注册成功');

            let _this = this;
            _this.$router.push({path:'/login'});
          }else {
            message.error(res.data.message);
          }
        });
      }
    },

  },
  mounted() {
    const self = this
    self.makeCode(this.identifyCodes, 4);
  },
  created() {

  },

}
</script>

<style scoped>
.login_container {
  background-image: linear-gradient(-180deg, #142b52 0%, #26596a 100%);
  /*background-image: url("../images/bg_login.png");*/
  background-repeat: no-repeat;
  background-size: cover;
  height: 100%;
}
.login_box {
  width: 290px;
  height: 400px;
  /* background-color: #fff; */
  background-color: #2e527bb3;
  border-radius: 5px;

  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}
.el-form {
  padding: 32px;
  position: absolute;
  bottom: 0;
  width: 100%;
  box-sizing: border-box;
}
.el-button {
  width: 100%;
}
.code {
  width: 45%;
}
img {
  /* style="width: 100px; height: 30px; margin-left:5px;vertical-align: middle;" */
  display:inline;
  width: 45%;
  height: 28px;
  margin-left: 10px;
  vertical-align: middle;
  border-radius: 3px;
}
.rememberMe {
  color: #fff;
}
</style>