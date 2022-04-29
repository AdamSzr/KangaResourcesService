// Next.js API route support: https://nextjs.org/docs/api-routes/introduction
import type { NextApiRequest, NextApiResponse } from "next";
import path from "path";
import {
  directoryExist,
  getDirStruct,
  getObjectInfo as getFileObjectInfo,
  isDir,
} from "../../../utils/file";
import { readFile } from "fs/promises";
import {
  BAD_SIZE_FORMAT,
  LIST_DIRECTORY_ERROR,
  NO_CONTENT,
  WRONG_PATH,
} from "../../../errors/errors";
import mime from 'mime-types'
import sharp from 'sharp'
import { catalogueAnalizer as directoryAnalizer } from "../../../utils/directoryAnalizer";
import { ImageSize } from "../../../models/ImageSize";

export const PUBLIC_DIR_ABS_PATH = path.join(process.cwd(), "public/data");

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  /** Full path that can be either dir of file */ 
  const filePath = path.join(PUBLIC_DIR_ABS_PATH, ...req.query.rest??"");
  const listDir = Boolean(req.query.list) ;
  const size = req.query.size as string;
  try {
    const { dir, base } = path.parse(filePath);

    if (!directoryExist(dir)) return res.status(404).send(WRONG_PATH);

    const { fullPath, requestedItem } =
      await getFileObjectInfo(dir, base);

    if (!requestedItem) return res.status(400).send(NO_CONTENT);

    if (isDir(fullPath))
      if (listDir) {
        const dirInfo = directoryAnalizer(filePath, await getDirStruct(filePath));
        return res.status(200).send(dirInfo);
      } else return res.status(400).send(LIST_DIRECTORY_ERROR);
 
    res.setHeader(
      "Content-Disposition",
      `attachment; filename=${requestedItem}`
    );

    let buffer = await readFile(fullPath);

    if(size)
    buffer = await tryResizeImage(res,requestedItem,buffer,size) as Buffer
    
    return res.status(200).send(buffer);
  } catch (err: any) {
    return res.status(404).send(err.message);
  }
}


function tryResizeImage(res:NextApiResponse,requestedItem:string,buffer:Buffer,size:string){
  const mimeType = mime.lookup(requestedItem)
  if(mimeType && mimeType.startsWith('image') )
  {
    const targetSize = ImageSize.parse(size)
    console.log(targetSize)
    if(!targetSize)
    return buffer

    return sharp(buffer).resize(targetSize.width,targetSize.height,{fit:"fill"}).toBuffer()
    // return res.status(400).json(BAD_SIZE_FORMAT)

    // return res.status(200).send(sharp(buffer).resize(targetSize.width,targetSize.height,{fit:"fill"})) 
  }
}