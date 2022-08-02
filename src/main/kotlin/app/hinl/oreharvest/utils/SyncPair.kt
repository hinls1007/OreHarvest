package app.hinl.oreharvest.utils

data class SyncPair(val setFunc: (Any) -> Unit, val getFunc: (Any) -> Unit)