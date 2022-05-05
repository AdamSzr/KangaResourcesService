package com.data.service.utils

fun getItemWithoutExt(fullPath: String): String {
    val itemWithExt = fullPath.substringAfterLast('/')
    val itemWithoutExt = itemWithExt.substringBeforeLast('.')
    return itemWithoutExt
}