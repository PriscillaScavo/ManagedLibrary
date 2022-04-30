import React, {useState} from 'react';
import Book from './Book'
import {generateListBooks} from './FuncLibrary'
import axios from 'axios';
import {nanoid} from "nanoid"
import { List, Typography, Divider } from 'antd';

export default function Library(){
    const [books, setBooks] = useState([])
    const [showBooks, setShowBooks] = useState(false)
    const [addMode, setAddMode] = useState(false)
    const [requestDb, setRequestDb] = useState(false)
    const [newBook, setNewBook] = useState({
        name: "",
        author: "",
        id: ""
    })

    React.useEffect(() => {
        async function getListBooks() {
            const res = await fetch("/library/books")
            const data = await res.json()
            const listBook = []
            if(data._embedded){
                data._embedded.oneBooks.map(book =>{
                    listBook.push({
                        name: book.name,
                        author: book.author,
                        id: book.idFE
                    })
                })
            }
            setBooks(listBook)
        }
        getListBooks()
    },[requestDb])

    async function removeBooks(id){
        await axios.delete('/library/books/' + id)
        .then(() => console.log("delete complete"));
        setRequestDb(old => !old)
    }

    function retrieveBooksList(){
        setShowBooks(oldFlag => {
            if(!oldFlag){
                setRequestDb(old => !old)
                return true
            }
            return false
        })
    }

    async function addBook(){
        const upload = {nameAuthor:  newBook.name + " by " + newBook.author}
        axios.post('/library/books/', upload)
         .then(response => console.log("post complete"));
        setNewBook({
                name: "",
                author: ""
            })
        setRequestDb(old => !old)
        setAddMode(false)
    }
    const booksList =
       books.map(book =>{
            return(
               <Book
                   id={book.id}
                   name = {book.name}
                   author = {book.author}
                   removeHandler = {() => removeBooks(book.id)}
                   key={book.id}
               />)
            })

    function handleChange(event) {
        const {name, value} = event.target
        setNewBook(oldBook => ({
            ...oldBook,
            [name]: value
        }))
    }

    const insertBookInfo =
    <div>
        <input
            type="text"
            placeholder="Title"
            className="form--input"
            name="name"
            value={newBook.name}
            onChange={handleChange}
        />
        <input
            type="text"
            placeholder="Author"
            className="form--input"
            name="author"
            value={newBook.author}
            onChange={handleChange}
        />
    </div>
  /*  return (
        <div>
            <button onClick = {retrieveBooksList}>All Books</button>
            {showBooks && <button onClick = {() => setAddMode(true)}>Add book</button>}
            {addMode &&
            <div>
                 {insertBookInfo}
                <button onClick = {addBook}>Add</button>
             </div>}
            {showBooks && booksList}
        </div>
    )*/
    return(
        <List
            size="large"
            header={<div>Header</div>}
            footer={<div>Footer</div>}
            bordered
            dataSource={booksList}
            renderItem={(item) => (
              <List.Item extra={<button size="small">Delete</button>}>
                {item}
              </List.Item>
            )}
        />
    )
}