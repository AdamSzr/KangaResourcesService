import { RequestError } from "../models/RequestError";

export const MISSING_PATH_QUERY =  {
    code: 1,
    message: "Query parameter ?path= is not provided"
} as RequestError

export const LIST_DIRECTORY_ERROR =  {
    code: 2,
    message: "Requested path directs to directory. Use ?list=true to list directory."
} as RequestError

export const WRONG_PATH =  {
    code: 3,
    message: "Requested path does not exist."
} as RequestError

export const NO_CONTENT =  {
    code: 4,
    message: "Under this path does not exist specyfied resources, go back to upper dir and list files"
} as RequestError


export const BAD_SIZE_FORMAT =  {
    code: 5,
    message: "Correct size format should be ?size=50x50"
} as RequestError