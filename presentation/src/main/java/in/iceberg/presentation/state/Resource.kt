package `in`.iceberg.presentation.state

class Resource<T>(val status: ResourceState, val data: T?, val error: String?)
