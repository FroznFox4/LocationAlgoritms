package Services.PopularPointsService

import Services.Services

interface PopularPointsServiceGen<T, O>: Services {
    fun getPopularPointsForUser(dots: T): O
    fun getPopularPointsForUser(dots: Map<String, T>): O
    fun getPopularPointsForUser(user: String, dots: T): O
    fun getPopularPointsForUsers(dots: Map<String, T>): Map<String, O>
    fun getPopularPointsForUserForPeople(user: O): O
}