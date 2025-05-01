package com.trivaris.votechain.di

import com.trivaris.votechain.models.candidate.CandidateRepository
import com.trivaris.votechain.models.vote.VoteRepository
import com.trivaris.votechain.viewmodels.CountingViewModel
import com.trivaris.votechain.viewmodels.VotingViewModel
import org.koin.dsl.module

val votingModule = module {
    single { CandidateRepository() }
    single { VoteRepository() }

    factory { VotingViewModel(get(), get()) }
    factory { CountingViewModel(get(), get()) }
}