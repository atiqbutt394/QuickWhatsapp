package com.atiq.quickwhatsapp.data.repository

import com.atiq.quickwhatsapp.data.db.RecentNumberDao
import com.atiq.quickwhatsapp.data.db.TemplateDao
import com.atiq.quickwhatsapp.model.MessageTemplate
import com.atiq.quickwhatsapp.model.RecentNumber
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val recentNumberDao: RecentNumberDao,
    private val templateDao: TemplateDao
) {
    val recentNumbers: Flow<List<RecentNumber>> = recentNumberDao.getAll()
    val templates: Flow<List<MessageTemplate>> = templateDao.getAll()

    suspend fun saveRecent(recentNumber: RecentNumber) = recentNumberDao.insert(recentNumber)
    suspend fun deleteRecent(id: Int) = recentNumberDao.deleteById(id)
    suspend fun clearRecents() = recentNumberDao.deleteAll()

    suspend fun saveTemplate(template: MessageTemplate) = templateDao.insert(template)
    suspend fun deleteTemplate(template: MessageTemplate) = templateDao.delete(template)

}