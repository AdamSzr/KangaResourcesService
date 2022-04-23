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
  public error
  constructor(filePath:string,baseDir:string,isDir:boolean, error:boolean, innerItems:string[]){
    this.filePath = filePath
    this.baseDir = baseDir
    this.isDir = isDir
    this.items = innerItems
    this.error = error
  }

}

export async function findRequestedFile(filePath: string) {
    const file = path.join(PUBLIC_DIR_ABS_PATH, filePath);
    const { dir, name } = path.parse(file);

    if(!fs.existsSync(dir))
    return new ObjectInfo("",dir,false,true,[])

    const files = await readdir(dir);
    console.log({ name, dir, files });
    
    const requestedFile = files.find((f) => path.parse(f).name == name);
    console.log({requestedFile})

    if(!requestedFile)
    return new ObjectInfo("",dir,false,true,files)

    if(isDir(requestedFile))
    return  new ObjectInfo("",requestedFile,true,false ,await readdir(requestedFile))

    const out = new ObjectInfo(file,dir,isDir(file),false,files)

    return out
}
