package com.example.todocompose.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    private const val SUPABASE_URL = "https://yxngozwcruheenpifuve.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inl4bmdvendjcnVoZWVucGlmdXZlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTQwMzAzNjcsImV4cCI6MjA2OTYwNjM2N30.Mh5wzZhDmrjYns3-nVyNGnuo8B1VPH6Fl9fqw0Tczrw" // Найди в настройках проекта Supabase

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest){serializer = KotlinXSerializer() }  // Для запросов к БД
        install(Realtime)   // Для real-time чата (опционально)
        install(Storage)    // Для загрузки файлов
    }
}