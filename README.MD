# Project Information

一个调试框架，主要包含，
* 快速查看应用信息：版本，渠道等
* 一些动态调试开关
* 查看当前应用隐藏目录/data/data or cache 等
* 界面化操作sp xml
* 直接操作db操作
* 显示当前activity视图

## Getting Started

```
allprojects {
    repositories {
        google()
        jcenter()
        maven{ url "http://www.momoda.pro:8081/repository/maven/"}
    }
}
dependencies {
    implementation 'cz.developer:developer-library:1.0.0'
}


//配置在application类中

 developer(this){
    //配置渠道
    channel="Channel"
    //数据切换
    switch {
        item(key="log",desc="日志调试")
        item(key="network",desc="网络请求")
        item(key="data",desc="数据调试")
        onItemChecked { _, _ ->  }
    }
}
```

Sample[下载](https://github.com/momodae/Developer/blob/master/apk/app-debug.apk?raw=true)

## Sample Picture
![](https://github.com/momodae/Developer/blob/master/image/image6.jpg?raw=true)
![](https://github.com/momodae/Developer/blob/master/image/image7.jpg?raw=true)
![](https://github.com/momodae/Developer/blob/master/image/image1.jpg?raw=true)
![](https://github.com/momodae/Developer/blob/master/image/image2.jpg?raw=true)
![](https://github.com/momodae/Developer/blob/master/image/image3.jpg?raw=true)
![](https://github.com/momodae/Developer/blob/master/image/image4.jpg?raw=true)
![](https://github.com/momodae/Developer/blob/master/image/image5.jpg?raw=true)

## Built With
* org.jetbrains.kotlin:kotlin-stdlib:1.3.21

## About developer 
> 可以以BuildConfig作动态配置，是否在线上版本放开此功能，或者直接以testImplementation编译，保证不影响线上版本
## Authors

* **CZ** - [MyWebsite](http://www.momoda.pro)
