package ru.geekbrains.weather.view.contentprovider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import ru.geekbrains.weather.MyApp.Companion.getHistoryDAO
import ru.geekbrains.weather.room.*

const val ENTITY_PATH = "HistoryEntity"
const val URI_ALL = 0
const val URI_ID = 0

class EducationContentProvider : ContentProvider() {

    private val authorities = "ru.geekbrains.weather.provider"
    private lateinit var uriMatcher: UriMatcher
    private var entityContentType: String? = null
    private var entityContentItemType: String? = null
    private lateinit var contentUri: Uri

    override fun onCreate(): Boolean {
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        uriMatcher.addURI(this.authorities, ENTITY_PATH, URI_ALL)
        uriMatcher.addURI(this.authorities, "$ENTITY_PATH/#", URI_ID)

        entityContentType = "vnd.android.cursor.dir/vnd.$authorities.$ENTITY_PATH"
        entityContentItemType = "vnd.android.cursor.item/vnd.$authorities.$ENTITY_PATH"

        contentUri = Uri.parse("content://$authorities/$ENTITY_PATH")

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sordOrder: String?
    ): Cursor {
        val historyDAO: HistoryDAO = getHistoryDAO()
        val pointer = when (this.uriMatcher.match(uri)) {
            URI_ALL -> {
                historyDAO.getHistoryPointer()
            }
            URI_ID -> {
                val id = ContentUris.parseId(uri)
                historyDAO.getHistoryPointer(id)
            }
            else -> throw IllegalStateException("impossibiru")
        }
        pointer.setNotificationUri(context?.contentResolver, contentUri)
        return pointer
    }

    override fun getType(uri: Uri): String? {
        return when (this.uriMatcher.match(uri)) {
            URI_ALL -> {
                entityContentType
            }
            URI_ID -> {
                entityContentItemType
            }
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        require(uriMatcher.match(uri) == URI_ALL) {
            "contentEntity type mismatch in $uri"
        }
        val historyDAO: HistoryDAO = getHistoryDAO()
        val entity = map(values)
        historyDAO.insertHistoryEntity(entity)
        val resultUri = ContentUris.withAppendedId(contentUri, entity.id)
        context?.contentResolver?.notifyChange(resultUri, null)
        return resultUri
    }

    private fun map(values: ContentValues?): HistoryEntity {
        return if (values == null)
            HistoryEntity()
        else {
            val id = if (values.containsKey(ID)) values[ID] as Long else 0
            val name = if (values.containsKey(NAME)) values[NAME] as String else ""
            val temperature = if (values.containsKey(TEMPERATURE)) values[TEMPERATURE] as Int else 0
            HistoryEntity(id, name, temperature)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        require(uriMatcher.match(uri) == URI_ALL) {
            "contentEntity type mismatch in $uri"
        }
        val historyDAO: HistoryDAO = getHistoryDAO()
        val id = ContentUris.parseId(uri)
        historyDAO.deleteHistoryEntity(id)
        context?.contentResolver?.notifyChange(uri, null)
        return 1
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectiinArgs: Array<out String>?
    ): Int {
        require(uriMatcher.match(uri) == URI_ALL) {
            "contentEntity type mismatch in $uri"
        }
        val historyDAO: HistoryDAO = getHistoryDAO()
        val entity = map(values)
        historyDAO.updateHistoryEntity(entity)
        context?.contentResolver?.notifyChange(uri, null)
        return 1
    }
}