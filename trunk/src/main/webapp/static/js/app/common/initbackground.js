/**
 * Created by Watcher on 16/6/29.
 */

$(function(){
    $('body').watermark({
        columnWidth: 200,       //每个工号为一列，设置列宽，默认为200像素宽
        rowSpacing: 100,        //每行之间的间距，默认为100像素的间距
        watermarkOpacity: 0.15, //水印文字的透明度，默认为0.2
        watermarkSize: 30,        //水印文字的大小，默认为40像素
        watermarkDegree: -30,
        contentOpacity: 0.1      //为了能看到水印背景，需设置网页内容的背景透明度，默认为0.7
    });
});


