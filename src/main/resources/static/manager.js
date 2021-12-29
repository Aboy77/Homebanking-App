const app = Vue.createApp({
        data() {
                return {
                        clientes: [],
                        firstName: "",
                        lastName: "",
                        email: "",
                        html: "",
                        modifyB: false,
                        modifyConfirm: false,
                        modifySelected: [],
                        changes: [],
                        sendPut: false,
                        inputsActive: [],
                        IDs: [],
                        deleteButton: false,
                        values: {
                                firstName: false,
                                lastName: false,
                                email: false
                        },
                        save: []
                }
        },
        created() {
                this.loadData();
        },
        computed: {

        },
        methods: {
                loadData() {
                        axios.get("/api/clients")
                                .then(data => {
                                        console.log(data.data)
                                        this.clientes = data.data;
                                        this.html = JSON.stringify(data.data);
                                });
                },
                send(e) {
                        e.preventDefault();
                        axios.post("http://localhost:8080/clients", {
                                firstName: this.firstName,
                                lastName: this.lastName,
                                email: this.email
                        }).then(response => {
                                this.clientes.push(response.data)
                        })
                                .catch(err => console.log(err))
                        this.loadData();

                        this.firstName = "";
                        this.lastName = "";
                        this.email = "";

                        window.location.reload();
                },
                borrar() {
                        this.deleteButton = true;
                        this.modifyB = false;
                        let modify = document.querySelector("input[value='Modificar']")
                        modify.disabled = true;
                },
                modiButton() {
                        this.deleteButton = false;
                        this.modifyB = true;
                        let deletes = document.querySelector("input[value='Borrar']")
                        deletes.disabled = true;
                },

                // buttons section

                acept() {
                        if (this.modifyB) {
                                this.modifyConfirm = true;
                                this.modifyB = false;
                                this.sendPut = true;

                                this.modifySelected.forEach(id => {
                                        let inputs = document.querySelectorAll(`input[id="${id._links.client.href}"]`)
                                        this.inputsActive.push(inputs)
                                        this.IDs.push(id._links.client.href);
                                })
                        } else if (this.deleteButton) {
                                this.modifySelected.forEach(del => {
                                        axios.delete(del._links.client.href)
                                                .then(res => console.log(res))
                                                .catch(err => console.log(err))
                                })

                                // limpiar lista y desactivar booleanos
                                this.modifySelected = [];
                                this.changes = [];
                                this.modifyB = false;
                                this.modifyConfirm = false;
                                this.sendPut = false;

                                this.loadData();
                                window.location.reload();
                        }
                },
                cancel() {
                        this.modifyB = false;
                        this.modifyConfirm = false;
                        this.deleteButton = false;
                        this.sendPut = false;
                        this.modifySelected = [];
                        this.changes = [];

                        // inputs 

                        let deletes = document.querySelector("input[value='Borrar']")
                        let modify = document.querySelector("input[value='Modificar']")
                        deletes.disabled = false;
                        modify.disabled = false;
                },
                inputs(e) {
                        const button = document.getElementById("modify");
                        let complete = false;
                        let id = this.modifySelected.filter(m => m._links.client.href === e.target.id);
                        id.forEach(i => {
                                switch (e.target.name) {
                                        case "firstName":
                                                i.firstName = e.target.value;
                                                if (e.target.value > 0) { }
                                                break;
                                        case "lastName":
                                                i.lastName = e.target.value
                                                break;
                                        case "email":
                                                i.email = e.target.value
                                                break;
                                }
                        })
                        this.inputsActive.forEach(input => {
                                // se verificara que cada input este con algun valor
                                for (let i = 0; i < input.length; i++) {
                                        if (input[i].value !== "") {
                                                complete = true;
                                        } else {
                                                complete = false
                                        }
                                }
                        })

                        if (complete) {
                                button.disabled = false;
                        } else {
                                button.disabled = true;
                        }
                },
                sendChanges() {
                        // iterar tanto como, id guardados y datos guardados en el array al momento de aceptar la modificacion
                        for (let i = 0; i < this.IDs.length; i++) {
                                axios.put(`${this.IDs[i]}`, {
                                        firstName: this.modifySelected[i].firstName,
                                        lastName: this.modifySelected[i].lastName,
                                        email: this.modifySelected[i].email
                                }).then(res => console.log(res))
                                        .catch(err => console.log(err))
                        }

                        // limpiar lista y desactivar booleanos
                        this.modifySelected = [];
                        this.changes = [];
                        this.modifyB = false;
                        this.modifyConfirm = false;
                        this.sendPut = false;

                        this.loadData();
                        window.location.reload();
                },
                verify(e) {
                        let input = document.querySelector("input[value='Enviar']");

                        switch (e.target.id) {
                                case "name":
                                        let str = e.target.value;
                                        let testF = /[a-zA-z]/g;
                                        let result = str.match(testF)
                                        
                                        //identificacion primeros caracteres no alfabeticos 
                                        if (result == null) {
                                                e.target.style.border = "1px solid red";
                                        } else {
                                                e.target.style.border = "1px solid green";
                                        }

                                        //identificacion de la cadena completa por comparacion de medida
                                        if(result.length === str.length) {
                                                e.target.style.border = "1px solid green";
                                                this.values.firstName = true;
                                        } else {
                                                e.target.style.border = "1px solid red";
                                                this.values.firstName = false;
                                        }
                                        break;
                                case "last":
                                        let strLast = e.target.value;
                                        let testL = /[a-zA-z]/g;
                                        let resultL = strLast.match(testL)

                                        if (resultL == null) {
                                                e.target.style.border = "1px solid red";
                                        } else {
                                                e.target.style.border = "1px solid green";
                                        }

                                        //identificacion de la cadena completa por comparacion de medida
                                        if(resultL.length === strLast.length) {
                                                e.target.style.border = "1px solid green";
                                                this.values.lastName = true;
                                        } else {
                                                e.target.style.border = "1px solid red";
                                                this.values.lastName = false;
                                        }
                                        console.log(resultL.length === strLast.length)
                                        break;
                                case "email":
                                        let strEmail = e.target.value;
                                        let testE = /\b[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}\b/g;
                                        let resultE = strEmail.match(testE)
                                        console.log(resultE)
                                        if (resultE == null) {
                                                e.target.style.border = "1px solid red";
                                        } else {
                                                e.target.style.border = "1px solid green";
                                                this.values.email = true;
                                        }
                                        break;
                        }

                        if (this.values.firstName && this.values.lastName && this.values.email) {
                                input.disabled = false;
                        } else {
                                input.disabled = true;
                        }
                },
                findClients(e) {
                        let filter = this.clientes.filter(c => c.firstName == e.target.value)
                        

                        if(filter.length > 0) {
                                this.clientes = [...filter]
                        } else {
                                this.clientes = [...this.save]
                        }
                        console.log(this.clientes)
                },
                saveClients() {
                        this.save = this.clientes
                }
        }
})

app.mount("#app");