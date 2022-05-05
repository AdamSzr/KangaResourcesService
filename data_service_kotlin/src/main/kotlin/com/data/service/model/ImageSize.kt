package com.data.service.model


class ImageSize(val width:Int, val height:Int)

fun parseImageSize(size:String?): ImageSize? {
    if(size==null)
        return null

    if(!size.contains('x'))
        return null

    val sizes = size.split('x')

    if(sizes.size!=2)
        return null

    return ImageSize(sizes[0].toInt(), sizes[1].toInt())
}