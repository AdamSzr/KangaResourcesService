// Next.js API route support: https://nextjs.org/docs/api-routes/introduction
import type { NextApiRequest, NextApiResponse } from "next";
import path from "path";
import { getDirStruct, getObjectInfo } from "../../../utils/file";
import { readFile } from "fs/promises";
import { PUBLIC_DIR_ABS_PATH } from "./[...rest]";


export default async function handler(
    req: NextApiRequest,
    res: NextApiResponse
) {
    res.status(200).json(await getDirStruct(PUBLIC_DIR_ABS_PATH))
}
