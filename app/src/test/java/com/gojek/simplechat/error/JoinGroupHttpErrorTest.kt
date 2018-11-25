package com.gojek.simplechat.error

import com.gojek.simplechat.constants.Constant
import com.gojek.simplechat.error.model.ErrorResponse
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException
import retrofit2.Response

class JoinGroupHttpErrorTest {

    @Test
    fun shouldThrowIOExceptionWhenResponseIsIOException() {
        val joinGroupHttpError = JoinGroupHttpError(IOException())
        assertEquals(Constant.IO_EXCEPTION, joinGroupHttpError.getErrorResponseMessage())
    }

    @Test
    fun shouldThrowUnauthorizedAccessWhenResponseCodeIs401() {
        val unauthorizedAccessErrorResponse: Response<ErrorResponse> = Response.error(
                401,
                ResponseBody.create(
                        MediaType.parse("application/json"),
                        "{\"message\":[\"Unauthorized Access\"]}"
                )
        )
        val joinGroupHttpError = JoinGroupHttpError(HttpException(unauthorizedAccessErrorResponse))
        assertEquals(Constant.UNAUTHORIZED_ACCESS, joinGroupHttpError.getErrorResponseMessage())
    }

    @Test
    fun shouldThrowResourceNotFoundWhenResponseCodeIs404() {
        val resourceNotFoundResponse: Response<ErrorResponse> = Response.error(
                404,
                ResponseBody.create(
                        MediaType.parse("application/json"),
                        "{\"message\":[\"Group does not exist\"]}"
                )
        )
        val joinGroupHttpError = JoinGroupHttpError(HttpException(resourceNotFoundResponse))
        assertEquals(Constant.GROUP_DOES_NOT_EXIST, joinGroupHttpError.getErrorResponseMessage())
    }

    @Test
    fun shouldThrowConflictErrorWhenResponseCodeIs409() {
        val conflictErrorResponse: Response<ErrorResponse> = Response.error(
                409,
                ResponseBody.create(
                        MediaType.parse("application/json"),
                        "{\"message\":[\"You already joined this group\"]}"
                )
        )
        val joinGroupHttpError = JoinGroupHttpError(HttpException(conflictErrorResponse))
        assertEquals(Constant.YOU_ALREADY_JOIN_THE_GROUP, joinGroupHttpError.getErrorResponseMessage())
    }


    @Test
    fun shouldThrowUnexpectedErrorWhenTheErrorCodeIsUnexpected() {
        val unexpectedErrorResponse: Response<ErrorResponse> = Response.error(
                500,
                ResponseBody.create(
                        MediaType.parse("application/json"),
                        "{\"message\":[\"Unexpected Error\"]}"
                )
        )
        val joinGroupHttpError = JoinGroupHttpError(HttpException(unexpectedErrorResponse))
        assertEquals(Constant.UNEXPECTED_ERROR, joinGroupHttpError.getErrorResponseMessage())
    }
}
