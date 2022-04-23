// Next.js API route support: https://nextjs.org/docs/api-routes/introduction
import type { NextApiRequest, NextApiResponse } from "next";
import path from "path";


export default async function handler(
    req: NextApiRequest,
    res: NextApiResponse
) {
    res.redirect('/api/data')
}
