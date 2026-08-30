package br.com.projeto.elo.data.remote

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class GeminiRequest(val contents: List<Content>)
data class Content(val parts: List<Part>)
data class Part(val text: String)

data class GeminiResponse(val candidates: List<Candidate>?)
data class Candidate(val content: Content?)

interface GeminiApi {
    @POST("v1beta/models/gemini-flash-lite-latest:generateContent")
    suspend fun classificarTransacao(
        @Header("X-goog-api-key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}
