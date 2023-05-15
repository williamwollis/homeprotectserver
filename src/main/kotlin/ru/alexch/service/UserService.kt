package ru.alexch.service

import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.id.toId
import ru.alexch.model.User


class UserService {

    private val client = KMongo.createClient()
    private val database = client.getDatabase("user")
    private val userCollection = database.getCollection<User>()
    fun create(user: User): Id<User>? {
        userCollection.insertOne(user)
        return user.id
    }
    fun findAll(): List<User> =
        userCollection.find()
            .toList()

    fun findById(id: String): User? {
        val bsonId: Id<User> = ObjectId(id).toId()
        return userCollection
            .findOne(User::id eq bsonId)
    }
    fun findByEmail(email: String): User? {
       // val nonCaseInsensitiveFilter = Person::email regex email
      //  val caseInsensitiveTypeSafeFilter = (Person::email).regex(email, "i")

        val nonTypeSafeFilter = "{email:{'\$regex' : '$email', '\$options' : 'i'}}"

       // val withAndOperator = personCollection.find(
        //    and(Person::email regex email, Person::age gt 40)
       // )

        //val implicitAndOperator = personCollection.find(
        //    Person::name regex name, Person::age gt 40
       // )

       // val withOrOperator = personCollection.find(
       //     or(Person::name regex name, Person::age gt 40)
        //)
        val userList = userCollection.find(nonTypeSafeFilter).toList()
        if(userList.isEmpty()) {
            return null
        }
        return userCollection.find(nonTypeSafeFilter)
            .toList()[0]
    }
    fun updateById(id: String, request: User): Boolean =
        findById(id)
            ?.let { person ->
                val updateResult = userCollection.replaceOne(person.copy(email = request.email, password = request.password, devices = request.devices))
                updateResult.modifiedCount == 1L
            } ?: false

    fun deleteById(id: String): Boolean {
        val deleteResult = userCollection.deleteOneById(ObjectId(id))
        return deleteResult.deletedCount == 1L
    }

}