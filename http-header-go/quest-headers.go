//https://github.com/pbyrne/request-headers/blob/master/request-headers.go
package main

import (
	"fmt"
	"net/http"
	"sort"
)

func main() {
	http.HandleFunc("/", printResquestHeaders)
	http.ListenAndServe(":5000", nil)
}

func printResquestHeaders(w http.ResponseWriter, r *http.Request) {
	for _, k := range sortedKeys(r.Header) {
		fmt.Fprintf(w, "%s: %s\n", k, r.Header[k])
	}
}

func sortedKeys(header http.Header) []string {
	keys := []string{}
	for k, _ := range header {
		keys = append(keys, k)
	}
	sort.Strings(keys)
	return keys
}
