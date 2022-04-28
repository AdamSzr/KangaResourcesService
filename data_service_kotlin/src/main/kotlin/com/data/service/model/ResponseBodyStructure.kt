package com.data.service.model

class ResponseBodyStructure (val path:String){
    val innerObjects = mutableListOf<DiskObject>()

    public fun addObject(obj:DiskObject){
        innerObjects.add(obj)
    }

    override fun toString(): String {
        return " \"path\":\"${path}\" , "
    }
}

//DiscObject = {
//    type: "DIR" | "FILE"
//    name: string
//}
//
//ResponseBodyStructure = {
//    path: string
//    items: DiscObject[]
//}