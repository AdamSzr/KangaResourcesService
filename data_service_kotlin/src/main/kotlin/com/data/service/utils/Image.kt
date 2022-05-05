package com.data.service.utils

import com.data.service.model.ImageSize
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

fun resizeImage(imageFile: File, size: ImageSize):ByteArray
{
    var originalImg = ImageIO.read(imageFile)
    val resizedImg = originalImg.getScaledInstance(size.width,size.height, Image.SCALE_DEFAULT)
    val baos =  ByteArrayOutputStream()
    val bufferedImage = BufferedImage(size.width,size.height, BufferedImage.SCALE_DEFAULT)
    bufferedImage.graphics.drawImage(resizedImg,0,0,null)
    ImageIO.write(bufferedImage,"jpg", baos)
    return baos.toByteArray()
}