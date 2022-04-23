// Next.js API route support: https://nextjs.org/docs/api-routes/introduction
import type { NextApiRequest, NextApiResponse } from "next";
import path from "path";
import { fileExists } from "../../../utils/file";
import { readFile } from "fs/promises";

export default async function handler(
    req: NextApiRequest,
    res: NextApiResponse
) {
    const filePath = path.join(...req.query.rest);
    console.log("Searching for: ", path.parse(filePath).name);

    const fullFilePath = await fileExists(filePath);
    if (!fullFilePath) return res.status(404).send("404 Not Found");
    console.log({fullFilePath})
    const buffer = await readFile(fullFilePath)
    const baseFile = path.parse(fullFilePath).base
    res.setHeader('Content-Disposition', `attachment; filename=${baseFile}`);
    res.status(200).send(buffer)
}
