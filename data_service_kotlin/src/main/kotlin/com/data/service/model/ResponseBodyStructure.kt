package com.data.service.model



class DiskObject (val name:String,val type: DiskObjectType){

}
class ResponseBodyStructure (val path:String){
    val innerObjects = mutableListOf<DiskObject>()
    public fun addObject(obj:DiskObject){
        innerObjects.add(obj)
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