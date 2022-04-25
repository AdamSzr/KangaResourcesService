import { RequestError } from "../models/RequestError";

export const LIST_DIRECTORY_ERROR =  {
    code: 1,
    message: "Requested path directs to directory. Use ?list=true to list directory."
} as RequestError

export const WRONG_PATH =  {
    code: 2,
    message: "Requested path does not exist."
} as RequestError

export const NO_CONTENT =  {
    code: 3,
    message: "Under this path does not exist specyfied resources, go back to upper dir and list files"
} as RequestError