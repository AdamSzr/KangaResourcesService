// Next.js API route support: https://nextjs.org/docs/api-routes/introduction
import type { NextApiRequest, NextApiResponse } from "next";
import path from "path";
import { findRequestedFile as getFileObjectInfo } from "../../../utils/file";
import { readFile } from "fs/promises";

export default async function handler(
    req: NextApiRequest,
    res: NextApiResponse
) {
    const filePath = path.join(...req.query.rest);
    console.log("Searching for: ", path.parse(filePath).name);

    const fileObjectInfo = await getFileObjectInfo(filePath);

    // console.log({fullFilePath})
    if(fileObjectInfo.error)
    return res.status(404).send("404 Not Found")

    
    if (fileObjectInfo.isDir) return res.status(400).send(fileObjectInfo.items);

    try{
        console.log(fileObjectInfo.filePath)
        const buffer = await readFile(fileObjectInfo.filePath)
        const baseFile = path.parse(fileObjectInfo.filePath).base
        res.setHeader('Content-Disposition', `attachment; filename=${baseFile}`); // set proper (same as on server) file with ext for saving purpose 
        res.status(200).send(buffer)
    }catch(error){
        console.error(error)
    }
}
