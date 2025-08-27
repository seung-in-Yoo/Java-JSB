export function getAuthHeader() {
    const token = localStorage.getItem("jwt");
    return token ? { "Authorization": `Bearer ${token}` } : {};
}