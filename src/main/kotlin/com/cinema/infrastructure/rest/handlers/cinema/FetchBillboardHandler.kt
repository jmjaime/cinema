package com.cinema.infrastructure.rest.handlers.cinema

import com.cinema.domain.actions.FetchBillboard
import com.cinema.infrastructure.rest.models.BillboardModel
import com.cinema.infrastructure.rest.models.toModel

class FetchBillboardHandler(private val fetchBillboard: FetchBillboard) {

    operator fun invoke(): BillboardModel {
        return fetchBillboard().toModel()
    }

}
