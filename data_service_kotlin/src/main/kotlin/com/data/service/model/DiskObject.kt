package com.data.service.model


class DiskObject (val name:String,val type: DiskObjectType){

    override fun toString(): String {
        return "{ \"name\":${name}, \"type\":$type }"
    }
}