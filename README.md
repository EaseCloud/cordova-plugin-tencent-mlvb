# Cordova Tencent MLVB Plugin

腾讯云《移动直播》集成 Cordova 插件

## Using

Create a new Cordova Project

    $ cordova create hello com.example.helloapp Hello
    
Install the plugin

    $ cd hello
    $ cordova plugin add https://github.com/easecloud/cordova-plugin-tencent-mlvb.git
    

Edit `www/js/index.js` and add the following code inside `onDeviceReady`

```js
    TencentMLVB.startPush(url);
```

Install iOS or Android platform

    cordova platform add ios
    cordova platform add android
    
Run the code

    cordova run 

## More Info

For more information on setting up Cordova see [the documentation](http://cordova.apache.org/docs/en/latest/guide/cli/index.html)

For more info on plugins see the [Plugin Development Guide](http://cordova.apache.org/docs/en/latest/guide/hybrid/plugins/index.html)
