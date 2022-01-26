import com.apollographql.apollo3.ApolloClient

val apolloClient: ApolloClient = ApolloClient.Builder()
    .serverUrl("https://apollo-fullstack-tutorial.herokuapp.com/graphql")
    .build()