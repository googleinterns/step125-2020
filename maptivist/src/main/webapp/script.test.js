const categories = require("./script")
const objects = [
    {
        name: "Pride Month",
        category: "LGBTQ",
    },

    {
        name: "Black Intern Week",
        category: "BLM",
    },

    {
        name: "Coming out in Tech",
        category: "LGTBQ",
    }
]

const blackInternWeek =     {
        name: "Black Intern Week",
        category: "BLM",
    }

test('categories', () => {
    expect(categories("BLM")).toBe(blackInternWeek);
});