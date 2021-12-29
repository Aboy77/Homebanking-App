const app = Vue.createApp({
    data() {
        return {
            test: "testing",
            info: {
                FirstName: "",
                LastName: ""
            },
            form: true,
            register: false,
            finished: false
        }
    },
    created() {
        console.log("test")
    },
    methods: {
        formInputs(e) {
            let text = /[a-zA-Z]/gim;
            let matchText = e.target.value.match(text)

            switch (e.target.name) {
                case "email":
                    e.target.style.borderBottom = "2px solid #17aa6f"

                    let test = /\b[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}\b/g;
                    let match = e.target.value.match(test)

                    if (match !== null) {
                        if (match.length !== 0) {
                            this.info.user = e.target.value.toLowerCase();
                            e.target.style.border = "1px solid green";
                            e.target.style.backgroundColor = "white";
                            e.target.style.color = "black";
                        } else {
                            this.info.user = "";
                            e.target.style.border = "none";
                            e.target.style.backgroundColor = "#ff001cb5";
                            e.target.style.color = "white!important";
                        }
                    } else {
                        this.info.user = "";
                        e.target.style.border = "none";
                        e.target.style.backgroundColor = "#ff001cb5";
                        e.target.style.color = "white";
                    }
                    break;
                case "password":
                    e.target.style.borderBottom = "2px solid #17aa6f"

                    let testN = /[a-zA-Z0-9#]/g;
                    let matchN = e.target.value.match(testN)
                    if (matchN !== null) {
                        if (e.target.value.length === e.target.value.match(testN).length) {
                            this.info.password = e.target.value;
                            e.target.style.border = "1px solid green";
                            e.target.style.backgroundColor = "white";
                            e.target.style.color = "black";
                        } else {
                            this.info.password = "";
                            e.target.style.border = "none";
                            e.target.style.backgroundColor = "#ff001cb5";
                            e.target.style.color = "white";
                        }
                    } else {
                        this.info.password = "";
                        e.target.style.border = "none";
                        e.target.style.backgroundColor = "#ff001cb5";
                        e.target.style.color = "white";
                    }
                    break;
                case "FirstName":
                    e.target.style.borderBottom = "2px solid #17aa6f"

                    if (matchText !== null) {
                        if (matchText.length == e.target.value.length) {
                            this.info.FirstName = e.target.value;
                            e.target.style.border = "1px solid green";
                            e.target.style.backgroundColor = "white";
                            e.target.style.color = "black";
                        } else {
                            this.info.FirstName = "";
                            e.target.style.border = "none";
                            e.target.style.backgroundColor = "#ff001cb5";
                            e.target.style.color = "white";
                        }
                    } else {
                        this.info.FirstName = "";
                        e.target.style.border = "none";
                        e.target.style.backgroundColor = "#ff001cb5";
                        e.target.style.color = "white";
                    }
                    break;
                case "LastName":
                    e.target.style.borderBottom = "2px solid #17aa6f"

                    if (matchText !== null) {
                        if (matchText.length == e.target.value.length) {
                            this.info.LastName = e.target.value;
                            e.target.style.border = "1px solid green";
                            e.target.style.backgroundColor = "white";
                            e.target.style.color = "black";
                        } else {
                            this.info.LastName = "";
                            e.target.style.border = "none";
                            e.target.style.backgroundColor = "#ff001cb5";
                            e.target.style.color = "white";
                        }
                    } else {
                        this.info.LastName = "";
                        e.target.style.border = "none";
                        e.target.style.backgroundColor = "#ff001cb5";
                        e.target.style.color = "white";
                    }
                    break;
            }
        },
        logIn(e) {
            e.preventDefault()
            const model = document.querySelector(".model");
            model.style.display = "flex";
            let email = this.info.user.toLowerCase()
            axios.post('/api/login', `email=${email}&password=${this.info.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } }).then(response => {
                e.target.disabled = true;
                model.style.display = "none";
                window.location.href = "/web/accounts.html";
            }).catch(err => {
                model.style.display = "none";
                const div = document.getElementById("err");

                const email = document.querySelector("input[name='email']");
                const password = document.querySelector("input[name='password']");
                
                if(email.value == "" && password.value == "") {
                    div.innerHTML = "Empty fields"
                } else {
                    div.innerHTML = "Email or Password incorrect"
                }

                div.style.display = "block"

                setTimeout(() => {
                    div.style.display = "none";
                }, 3000)
            })
        },
        registerClient(e) {
            const div = document.querySelector(".model");
            div.style.display = "flex";
            e.preventDefault();
            axios.post('/api/clients', `firstName=${this.info.FirstName}&lastName=${this.info.LastName}&email=${this.info.user}&password=${this.info.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } }).then(response => {
                div.style.display = "none";

                swal({
                    title: "Account created!",
                    text: "Login to start!",
                    icon: "success",
                    button: "Ok",
                  }).then(res => {
                    this.info = {}
                    this.form = true
                    this.register = false
                  })
            }).catch(err => {
                div.style.display = "none";

                const error = document.getElementById("err");

                const email = document.querySelector("input[name='email']");
                const password = document.querySelector("input[name='password']");
                
                if(email.value == "" && password.value == "") {
                    error.innerHTML = "Empty fields"
                } else {
                    error.innerHTML = "Email or Password incorrect"
                }

                error.style.display = "block"

                setTimeout(() => {
                    error.style.display = "none";
                }, 3000)
            })
        },
        createAccount() {
            this.form = false;
            this.register = true;
        },
        logInPage() {
            this.form = true;
            this.register = false;
        }
    }
})

app.mount("#form");