package Services.PeoplesAroundService.Models

data class IntersectionsPeoples(
    val user: String = "",
    val users: MutableSet<String> = mutableSetOf())