package com.data.service.errors

import com.data.service.model.ErrorStructure

var ERROR_MISSING_QUERY = ErrorStructure(1,"\"Requested path directs to directory. Use ?list=true to list directory.\"")

var ERROR_REQUESTED_OBJ_IS_DIR = ErrorStructure(1,"\"Requested path directs to directory. Use ?list=true to list directory.\"")

var ERROR_REQUESTED_PATH_NOT_EXIST = ErrorStructure(1,"\"Requested path directs to directory. Use ?list=true to list directory.\"")