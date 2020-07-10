const { filterByCategory, filterByProximity } = require("./script");

describe("Filter function by category", () => {
    test("it should filter by category", () => {
        const input = [
            {
                name: "Pride Month",
                category: ["LGBTQ", "BLM"],
            },

            {
                name: "Black Intern Week",
                category: ["BLM"],
            },

            {   
                name: "Coming out in Tech",
                category: ["LGTBQ"],
            }];

        const output = [   
            {
                name: "Pride Month",
                category: ["LGBTQ", "BLM"],
            },
            
            {
                name: "Black Intern Week",
                category: ["BLM"],
            }
        ];
        
        expect(filterByCategory(input, "BLM")).toMatchObject(output);
    });
});

describe("Filter function by proximity", () => {
    test("it should filter by proximity", () => {
        const input = [
            {
                name: "Eiffel Tower",
                longitude: 2.2957289,
                latitude: 48.8539173, 
            },
            {
                name: "cristo",
                longitude: -43.2126759,
                latitude: -22.951911, 
            },
            {
                name: "buckingham",
                longitude: -0.1440787,
                latitude: 51.5013673, 
            },
            {
                name: "macchuPicchu",
                longitude: -72.5449629,
                latitude: -13.1631412, 
            },
        ];

        const output = [
            {
                name: "Eiffel Tower",
                longitude: 2.2957289,
                latitude: 48.8539173, 
            },
        ];
        
        // location of champs-elysees in france
        expect(filterByProximity(input, 48.8697953, 2.3056317)).toMatchObject(output);

    });
});
