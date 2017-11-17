# COMP2211
Java Ad Auction Dashboard

An ad-analytics dashboard built from the ground up in Java using JavaFX + SceneBuilder for the UI and a bespoke, high-performance data storage backend.

The backend reads CSV files and stores their values into their most efficient representations in memory reducing overhead introduced by Java objects. Data is stored in a way that optimises querying times by exploiting the cache to contain relevant data in the next cacheline. Queries were done using the most low-level operations as possible, i.e. bitwise operators. 
