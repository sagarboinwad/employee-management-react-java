import { useEffect, useState } from "react";

function App() {
  const [employees, setEmployees] = useState([]);

  // Add Employee form state
  const [id, setId] = useState("");
  const [name, setName] = useState("");
  const [salary, setSalary] = useState("");

  // Fetch employees from backend
  const fetchEmployees = () => {
    fetch("http://localhost:9090/employees")
      .then((res) => res.json())
      .then((data) => setEmployees(data))
      .catch(console.error);
  };

  useEffect(() => {
    fetchEmployees();
  }, []);

  // Add new employee
  const addEmployee = () => {
    if (!id || !name || !salary) {
      alert("Please fill all fields");
      return;
    }

    fetch("http://localhost:9090/add-employee", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ id: Number(id), name, salary: Number(salary) }),
    })
      .then((res) => res.text())
      .then((msg) => {
        alert(msg);
        setId("");
        setName("");
        setSalary("");
        fetchEmployees();
      })
      .catch(console.error);
  };

  // Delete employee
  const deleteEmployee = (empId) => {
    fetch("http://localhost:9090/delete-employee", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ id: empId }),
    })
      .then((res) => res.text())
      .then((msg) => {
        alert(msg);
        fetchEmployees();
      })
      .catch(console.error);
  };

  // Update employee salary
  const updateEmployeeSalary = (empId, currentSalary) => {
    const newSalary = prompt("Enter new salary:", currentSalary);
    if (!newSalary) return;

    fetch("http://localhost:9090/update-employee", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ id: empId, salary: Number(newSalary) }),
    })
      .then((res) => res.text())
      .then((msg) => {
        alert(msg);
        fetchEmployees();
      })
      .catch(console.error);
  };

  return (
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <h2>Employee List</h2>

      {/* Add Employee Form */}
      <div style={{ marginBottom: "20px" }}>
        <input
          type="number"
          placeholder="ID"
          value={id}
          onChange={(e) => setId(e.target.value)}
          style={{ marginRight: "10px" }}
        />
        <input
          type="text"
          placeholder="Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          style={{ marginRight: "10px" }}
        />
        <input
          type="number"
          placeholder="Salary"
          value={salary}
          onChange={(e) => setSalary(e.target.value)}
          style={{ marginRight: "10px" }}
        />
        <button onClick={addEmployee}>Add Employee</button>
      </div>

      {/* Employee Table */}
      <table style={{ borderCollapse: "collapse", width: "80%" }} border="1">
        <thead style={{ backgroundColor: "#4CAF50", color: "white" }}>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Salary</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {employees.map((emp, index) => (
            <tr
              key={emp.id}
              style={{ backgroundColor: index % 2 === 0 ? "#f9f9f9" : "#e6f7ff" }}
            >
              <td>{emp.id}</td>
              <td>{emp.name}</td>
              <td>{emp.salary}</td>
              <td>
                <button
                  onClick={() => updateEmployeeSalary(emp.id, emp.salary)}
                  style={{ marginRight: "10px" }}
                >
                  Update Salary
                </button>
                <button
                  onClick={() => deleteEmployee(emp.id)}
                  style={{ backgroundColor: "#ff4d4f", color: "white" }}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default App;
