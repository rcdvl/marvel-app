package com.rcdvl.marvel.networking

import java.io.Serializable
import java.util.*

/**
 * Created by renan on 3/17/16.
 */
data class MarvelResponse<T>(var code: String, var status: String, var copyright: String,
                             var attributionText: String, var attributionHTML: String,
                             var etag: String, var data: MarvelData<T>)

data class MarvelData<T>(var offset: Int, var limit: Int, var total: Int, var count: Int,
                         var results: ArrayList<T>)

data class MarvelCharacter(var id: Long, var name: String, var description: String,
                           var modified: String, var thumbnail: MarvelThumbnail,
                           var resourceURI: String, var comics: MarvelListWrapper,
                           var series: MarvelListWrapper, var stories: MarvelListWrapper,
                           var events: MarvelListWrapper, var urls: ArrayList<MarvelUrl>) : Serializable

data class MarvelThumbnail(var path: String, var extension: String) : Serializable

data class MarvelListWrapper(var available: Int, var collectionURI: String,
                             var items: ArrayList<MarvelListItem>, var returned: Int) : Serializable

data class MarvelListItem(var resourceURI: String, var name: String) : Serializable

data class MarvelUrl(var type: String, var url: String) : Serializable

//data class MarvelSeriesWrapper(var available: Int, var collectionURI: String,
//                               var items: ArrayList<MarvelSeries>)
//
//data class MarvelSeries(var resourceURI: String, var name: String)
//
//data class MarvelComic(var id: Int, var digitalId: Int, var title: String, var issueNumber: Double,
//                       var variantDescription: String, var description: String, var modified: String,
//                       var isbn: String, var upc: String, var diamondCode: String, var ean: String,
//                       var images: ArrayList<MarvelThumbnail>, var thumbnail: MarvelThumbnail)

data class MarvelResource(var id: Int, var title: String, var thumbnail: MarvelThumbnail,
                          var type: String) : Serializable

/*
{
        "id": "int",
        "digitalId": "int",
        "title": "string",
        "issueNumber": "double",
        "variantDescription": "string",
        "description": "string",
        "modified": "Date",
        "isbn": "string",
        "upc": "string",
        "diamondCode": "string",
        "ean": "string",
        "issn": "string",
        "format": "string",
        "pageCount": "int",
        "textObjects": [
          {
            "type": "string",
            "language": "string",
            "text": "string"
          }
        ],
        "resourceURI": "string",
        "urls": [
          {
            "type": "string",
            "url": "string"
          }
        ],
        "series": {
          "resourceURI": "string",
          "name": "string"
        },
        "variants": [
          {
            "resourceURI": "string",
            "name": "string"
          }
        ],
        "collections": [
          {
            "resourceURI": "string",
            "name": "string"
          }
        ],
        "collectedIssues": [
          {
            "resourceURI": "string",
            "name": "string"
          }
        ],
        "dates": [
          {
            "type": "string",
            "date": "Date"
          }
        ],
        "prices": [
          {
            "type": "string",
            "price": "float"
          }
        ],
        "thumbnail": {
          "path": "string",
          "extension": "string"
        },
        "images": [
          {
            "path": "string",
            "extension": "string"
          }
        ],
        "creators": {
          "available": "int",
          "returned": "int",
          "collectionURI": "string",
          "items": [
            {
              "resourceURI": "string",
              "name": "string",
              "role": "string"
            }
          ]
        },
        "characters": {
          "available": "int",
          "returned": "int",
          "collectionURI": "string",
          "items": [
            {
              "resourceURI": "string",
              "name": "string",
              "role": "string"
            }
          ]
        },
        "stories": {
          "available": "int",
          "returned": "int",
          "collectionURI": "string",
          "items": [
            {
              "resourceURI": "string",
              "name": "string",
              "type": "string"
            }
          ]
        },
        "events": {
          "available": "int",
          "returned": "int",
          "collectionURI": "string",
          "items": [
            {
              "resourceURI": "string",
              "name": "string"
            }
          ]
        }
      }
    ]
  }
 */