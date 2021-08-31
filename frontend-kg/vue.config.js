module.exports = {
    devServer: {
        port: 8000,
        proxy: {
            '/api': {
                target: 'http://localhost:8081/',   //接口域名
                changeOrigin: true,             //是否跨域
                pathRewrite: {                  //路径重置
                    '^/api': '/api'
                }
            }
        }
    },

    chainWebpack: config =>{
        config.plugin('html')
        .tap(args => {
            args[0].title = "CODING67";
            return args;
        })
    },
}