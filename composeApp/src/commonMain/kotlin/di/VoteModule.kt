package com.trivaris.votechain.di

import com.trivaris.votechain.models.vote.VoteRepository
import com.trivaris.votechain.network.FabricGatewayClient
import com.trivaris.votechain.viewmodels.CountingViewModel
import org.koin.dsl.module

val voteModule = module {
    single { FabricGatewayClient() }
    single { VoteRepository(get()) }
    factory { CountingViewModel(get()) }
}