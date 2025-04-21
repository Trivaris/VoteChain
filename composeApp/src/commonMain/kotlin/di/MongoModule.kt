package com.trivaris.votechain.di

import com.trivaris.votechain.models.candidate.CandidateRepository
import com.trivaris.votechain.viewmodels.VotingViewModel
import org.koin.dsl.module

val mongoModule = module {
    single { CandidateRepository() }
    factory { VotingViewModel(get()) }
}