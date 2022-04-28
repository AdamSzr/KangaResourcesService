package com.data.service.model

class ResponseBodyStructure(val path:String, val innerObject: List<DiskObject>){
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