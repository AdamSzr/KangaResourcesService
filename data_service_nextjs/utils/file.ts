import { readdir } from "fs/promises";
import fs, { read } from "fs";
import path from "path";

const PUBLIC_DIR_ABS_PATH = path.join(process.cwd(), "public/data");


function isDir(filePath:string){
  return path.parse(filePath as string).ext==""
}

class ObjectInfo{
  public filePath 
  public baseDir
  public isDir 
  public items
  constructor(filePath:string,baseDir:string,isDir:boolean, innerItems:string[]){
    this.filePath = filePath
    this.baseDir = baseDir
    this.isDir = isDir
    this.items = innerItems
  }

}

export async function getObjectInfo(filePath: string) {
    const fullPath = path.join(PUBLIC_DIR_ABS_PATH, filePath);
    const { dir, name } = path.parse(fullPath);

    if(!fs.existsSync(dir))
    throw new Error("Base file directory does not exist")

    const files = await readdir(dir);
    
    const requestedFile = files.find((f) => path.parse(f).name == name);

    if(!requestedFile)
    throw new Error(`Cannot find element '${name}' in directory ${dir}, choose one of [${files}]`)

    if(isDir(requestedFile))
      return new ObjectInfo("",fullPath,true,await readdir(fullPath))
  
    return new ObjectInfo(path.join(dir,requestedFile),dir,false,files)
}

export async function getBaseDirStruct(){
    return await readdir(PUBLIC_DIR_ABS_PATH);
}