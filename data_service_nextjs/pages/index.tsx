import type { NextPage } from 'next'
import { redirect } from 'next/dist/server/api-utils'
import Head from 'next/head'
import Image from 'next/image'
import Link from 'next/link'
import styles from '../styles/Home.module.css'

const Home = () => {
  return(<Link href="/api/data?path=&list=true"> GO TO RESOURCE SERVICE </Link>)
}

export default Home
