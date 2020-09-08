# Encrypted Search

*Encrypted search* is a phrase that is usually shorthand for the process of searching encrypted data for items that match a query string, without actually decrypting the data first. Another term commonly used for this capability is *searchable encryption*.

There are multiple techniques used to implement searchable encryption, and the appropriate technique depends on the requirements of the search. For instance, you might require the ability to search for text documents that contain one or more keywords, or you might need to search shorter strings for matching substrings.

A simple way to implement search functionality is to just scan all of the data each time you do a search. Unfortunately, this has very poor performance if there is much data, and it becomes even poorer if all the data is encrypted and you need to decrypt it first to scan for matches. Implementing a performant search usually involves creating some sort of index that can be searched more efficiently. This is less straightforward when the system is storing encrypted data; securing all the data with encryption then storing substrings or keywords from the data in plaintext compromises too much privacy and security. In order to mitigate this loss of security, it is necessary to obscure the data in the index as well.

IronCore’s SDKs provide the capability to perform a *substring search over short strings* using a *blind index* technique.

## Substring Search over Short Strings

This type of search allows you to find data that contains the search query as a substring, not necessarily as a full word. For example, a search for “car” should match both “My car is old” and “escargot”.

### Blind Index

A *blind index* is an approach that obscures the data stored in the search index. *Index terms* are extracted from the data before it is encrypted, then each of those terms is processed using a keyed hash to produce an *index token*, which is a representation that cannot be reversed to recover the plaintext. To keep the index data secure, the key for the hash should be different from the key used to encrypt the data, and the hash key should not be known to the service that stores the data.

When a client wants to search the stored data, it extracts index terms from the query, applies the hash algorithm with the same key to generate index tokens, then searches the blind index for matches.

### Partitioning the Index

It is not possible to access the tokens stored in the blind index directly to recover portions of the information in the encrypted data. The addition of the hidden key that is used to generate the index terms prevents an attacker with access to the blind index from generating a *rainbow table* (a list of the hashes of frequently occurring terms that can be matched against terms in the index). However, there is still some potential leakage of information that lessens the security of the encrypted data. It is still possible to use frequency analysis to guess which mappings of plaintext to index tokens are most likely, especially if the attacker knows the domain of the encrypted data (e.g. names or addresses). And if the attacker does know the plaintext for an item of indexed data, they can find other entries in the index that have matching tokens and identify part of the content of those indexed items.

In order to mitigate the impact of these attacks, IronCore’s blind index implementation allows you to associate a partition with each data item that is indexed. The partition name is added to each index term before it is hashed into a token, so that the same term in separate partitions generates a different token. If data can be partitioned into smaller buckets, frequency analysis becomes much less effective, and it is no longer possible to find matches across partitions.

Note that in order to use partitioning, a client that wants to search the index must be able to supply the appropriate partition name. Also, it is not possible to search across partitions.

### Indexing Data before Encrypting

When you have a blind index set up, your application can use the IronCore SDKs to generate a set of index tokens that represent the data in a string that you are going to encrypt to protect its contents. The SDK applies a process called *transliteration* to each string to convert it to a canonical form (all lowercase, punctuation characters removed, and characters converted to an string of equivalent ASCII characters). The transliterated string is then processed to generate a set of index tokens. Your application then stores these tokens associated with the record that contains the string, and it uses the tokens in searches.

### Searching Using the Index

If a caller wants to search a collection of encrypted data that has been indexed, it can use the IronCore SDK to generate a set of index tokens for the query string. Your application then searches the stored index tokens to find records that have all of the index tokens generated by your query string. Each record that matches should be returned to your client, which can use the IronCore SDK to decrypt the sensitive data in the record. Because we purposefully obscure some of the data in the index to make it more secure, your client will need to check each record to make sure that the record isn’t a *false positive* (a potential match that doesn’t actually contain the query string in the sensitive data) before displaying or processing it.

### Multiple Indices

A substring search index is best used to index a focused data set - a particular field or subset of fields from records in a database, for instance. It would be possible to use the same index for several disparate fields, or for fields from different types of records, but search results will be better if the user knows the domain of the search and can retrieve only records for that domain. To support this in applications that might handle data across multiple domains, the IronCore SDKs allow the application to create, initialize, index, and search using multiple search indices. For example, the application might use one index for the names of staff members and another index for the addresses of customers. The SDKs provide interfaces to facilitate management of multiple search indices.

### Securing the Index

To minimize the opportunities for an attack to gain access to the blind index where it is stored and use that to extract information about the encrypted data, it is essential to prevent the server that stores the data from also storing the hash key value that is used to generate the index tokens. Otherwise, rainbow tables can be constructed to recover information from the index.

We protect this hash key value using IronCore’s encryption features. Each of the IronCore Data Control Platform SDKs include a method to create a new blind index - the SDK requires the caller to provide the ID of an IronCore group that should be used to protect access to the index. IronCore's [orthogonal access control](https://ironcorelabs.com/docs/data-control-platform/concepts/orthogonal-access/) allows you to manage the membership in this group even after you have used it to control access to the index, so you can adjust the list of users that can access the search information at any time. The SDK generates a random value for the hash key, encrypts it to the specified group, and returns the encrypted value to the caller to be stored. The SDK also includes a method to initialize a previously created search index for use given the encrypted hash key. The function decrypts the value and holds it in memory to be used in any SDK methods that generate index tokens.

For the blind index to be used, each user that can index new data before it is encrypted or who can search encrypted data must be a member of the group that was used to encrypt the hash key.

## Further Information

If you are interested in more details about implementing encrypted search with IronCore's Data Control Platform, there are [use cases](https://ironcorelabs.com/docs/data-control-platform/guide#protecting-personal-data-that-is-used-for-record-location/) and [code patterns](https://ironcorelabs.com/docs/data-control-platform/guide#encrypted-search-patterns/) available in our [Data Control Platform Guide](https://ironcorelabs.com/docs/data-control-platform/guide/). The guide also includes links to the API documentation for our different Data Control Platform SDKs.

ON THIS PAGE

[Substring Search over Short Strings](https://ironcorelabs.com/docs/data-control-platform/concepts/encrypted-search/#substring-search-over-short-strings)[Blind Index](https://ironcorelabs.com/docs/data-control-platform/concepts/encrypted-search/#blind-index)[Partitioning the Index](https://ironcorelabs.com/docs/data-control-platform/concepts/encrypted-search/#partitioning-the-index)[Indexing Data before Encrypting](https://ironcorelabs.com/docs/data-control-platform/concepts/encrypted-search/#indexing-data-before-encrypting)[Searching Using the Index](https://ironcorelabs.com/docs/data-control-platform/concepts/encrypted-search/#searching-using-the-index)[Multiple Indices](https://ironcorelabs.com/docs/data-control-platform/concepts/encrypted-search/#multiple-indices)[Securing the Index](https://ironcorelabs.com/docs/data-control-platform/concepts/encrypted-search/#securing-the-index)[Further Information](https://ironcorelabs.com/docs/data-control-platform/concepts/encrypted-search/#further-information)

