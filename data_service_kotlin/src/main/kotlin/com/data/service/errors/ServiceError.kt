package com.data.service.errors

import com.data.service.model.ErrorStructure

var REQUESTED_OBJ_IS_DIR = ErrorStructure(1,"Requested path directs to directory. Use ?list=true to list directory")
val NO_CONTENT = ErrorStructure (2, "Under this path does not exist specified resources, go back to upper dir and list files")
var REQUESTED_PATH_NOT_EXIST = ErrorStructure(3,"Requested path does not exist")