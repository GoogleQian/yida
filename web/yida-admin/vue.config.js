// 接口地址baseUrl
const target = require('./src/config/end');
module.exports = {
    baseUrl: process.env.NODE_ENV === 'production' ? '/' : '/', // 部署应用程序的路径，如果为根路径就设置为'/'否则设置为对应目录，设置为空''时表示使用相对路径
    devServer: {
        proxy: {
            '/api': {   // 筛选所有/api请求、将^/api替换为''，并且加上前缀target
                target: target.dev,
                ws: true,
                changeOrigin: true,
                pathRewrite: {
                    '^/api': ''
                },
                // secure: false
            }
        },
        // https: true
    },
    productionSourceMap: false, // 不生成sourceMap文件
    configureWebpack: {
        externals: {
            // "BMap": "BMap",
            // "BMapLib": "BMapLib",
            "$": "$",
            "returnCitySN": "returnCitySN"
        },
    },
    // chainWebpack: config => {
        // config
        //     .plugin('html')
        //     .tap(args => {
        //         args[0].chunksSortMode = "none"
        //         return args
        //     })
    // }
}