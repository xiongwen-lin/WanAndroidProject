package com.apemans.custom.bean

import androidx.room.*


@Dao
interface ChatRecordDao {

    //Insert 可以返回插入数据的id值
    @Insert
    suspend fun insert(recordDataBean: RecordDataBean):Long

    @Delete
    suspend fun delete(recordDataBean: RecordDataBean): Int

    @Update
    suspend fun update(recordDataBean: RecordDataBean): Int

    @Query("select * from chat_record where user_account =:userAccount order by date asc")
    suspend fun queryAllRecord(userAccount:String): List<RecordDataBean>

    @Query("select  * from chat_record order by id desc limit 1")
    suspend fun queryLastRecord(): RecordDataBean

    @Query("select * from chat_record where content_type=6")
    suspend fun isExistHotQuestion(): RecordDataBean

    @Query("select * from chat_record where msg_id = :msgId")
    suspend fun isExistInDatabase(msgId:Int): RecordDataBean?


}