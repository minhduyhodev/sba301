import React, { useState, useEffect } from "react";
import { getOrchids } from "../api/orchidApi";

export default function ListOfOrchids() {
  const [orchids, setOrchids] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    let active = true;
    setLoading(true);

    getOrchids()
      .then(({ data }) => {
        if (active) setOrchids(data);
      })
      .catch((err) => {
        if (active) setError(err);
      })
      .finally(() => {
        if (active) setLoading(false);
      });

    return () => {
      active = false;
    };
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return (
      <div>
        <p>Error: {error.message || "Failed to load orchids"}</p>
      </div>
    );
  }

  if (orchids.length === 0) {
    return <div>No orchids available.</div>;
  }

  return (
    <div>
      <h2>List of Orchids</h2>
      <table border="1" cellPadding="6" cellSpacing="0">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Category</th>
            <th>Natural</th>
            <th>Attractive</th>
            <th>Description</th>
            <th>Image</th>
          </tr>
        </thead>
        <tbody>
          {orchids.map((orchid) => (
            <tr key={orchid.orchidID}>
              <td>{orchid.orchidID}</td>
              <td>{orchid.orchidName}</td>
              <td>{orchid.orchidCategory}</td>
              <td>{orchid.isNatural ? "Yes" : "No"}</td>
              <td>{orchid.isAttractive ? "Yes" : "No"}</td>
              <td>{orchid.orchidDescription}</td>
              <td>
                {orchid.orchidURL && (
                  <img
                    src={orchid.orchidURL}
                    alt={orchid.orchidName}
                    width="80"
                  />
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
