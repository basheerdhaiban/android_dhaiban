package com.semicolon.data.repository.remote.repository

import com.semicolon.data.repository.remote.mapper.toBalanceModel
import com.semicolon.data.repository.remote.mapper.toWalletHistoryModel
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.wallet.BalanceDto
import com.semicolon.data.repository.remote.model.wallet.WalletHistoryResponse
import com.semicolon.domain.entity.BalanceModel
import com.semicolon.domain.entity.WalletHistoryModel
import com.semicolon.domain.repository.WalletRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class WalletRepositoryImp(client: HttpClient) : BaseRepository(client), WalletRepository {
    companion object {
        const val YOUR_BALANCE = "yourBalance"
        const val WALLET_HISTORY = "walletHistory"
    }

    override suspend fun getCurrentBalance(): BalanceModel {
        val result = tryToExecute<BaseResponse<BalanceDto>> {
            client.get(YOUR_BALANCE)
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.toBalanceModel()
    }

    override suspend fun getWalletHistory(): List<WalletHistoryModel> {
        val result = tryToExecute<BaseResponse<WalletHistoryResponse>> {
            client.get(WALLET_HISTORY)
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.data.map { it.toWalletHistoryModel() }
    }
}