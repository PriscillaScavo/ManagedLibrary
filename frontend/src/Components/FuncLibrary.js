import React from 'react';
import axios from 'axios';

export function generateListBooks(){
    const listBook = []
    axios.get('/library/books')
        .then(response => response.data._embedded.oneBooks.map
        (book =>{
            listBook.push({
                name: book.name,
                author: book.author,
                id: book.idFE
            })
        }));
    return listBook;
}