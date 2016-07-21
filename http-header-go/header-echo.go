//https://github.com/Stackato-Apps/header-echo/blob/stackato-3.6/echo.go

package main

import (
	"fmt"
	"net/http"
	"os"
	"sort"
	"strings"
)

func handler(w http.ResponseWriter, r *http.Request) {
	var keys []string
	for k := range r.Header {
		keys = append(keys, k)
	}
	sort.Strings(keys)
	for _, k := range keys {
		v := strings.Join(r.Header[k], " ")
		fmt.Fprintf(w, "%s: %s\n", k, v)
	}
}

func main() {
	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
	}
	http.HandleFunc("/", handler)
	http.ListenAndServe(fmt.Sprintf(":%s", port), nil)
}
